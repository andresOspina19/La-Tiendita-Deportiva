package com.latienditadeportiva.Inventory_ms.controllers;

import com.latienditadeportiva.Inventory_ms.dto.cart.CartDto;
import com.latienditadeportiva.Inventory_ms.dto.cart.CartItemDto;
import com.latienditadeportiva.Inventory_ms.dto.order.OrderIdUsernameDto;
import com.latienditadeportiva.Inventory_ms.exceptions.*;
import com.latienditadeportiva.Inventory_ms.models.Cart;
import com.latienditadeportiva.Inventory_ms.models.OrderProduct;
import com.latienditadeportiva.Inventory_ms.models.Orders;
import com.latienditadeportiva.Inventory_ms.models.Product;
import com.latienditadeportiva.Inventory_ms.repositories.CartRepository;
import com.latienditadeportiva.Inventory_ms.repositories.OrdersRepository;
import com.latienditadeportiva.Inventory_ms.repositories.ProductRepository;
import com.latienditadeportiva.Inventory_ms.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class OrdersController {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    ///Pedido despues del pago
    @PostMapping("/order/addOrderByUsername/{username}")
    public ResponseEntity<ApiResponse> placeOrder(@PathVariable String username, @RequestParam String paymentToken) {
        //Primero debemos obtener los items del carrito
        List<Cart> cartList = cartRepository.findAllByUsernameOrderByCreatedDateDesc(username);
        List<CartItemDto> cartItems = new ArrayList<>();

        for (Cart cart:cartList) {
            Product cartProduct = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("No se encontró un producto con el ID: " + cart.getProductId()));

            //Verificamos que existan unidades del producto, sino, lo eliminamos automaticamente del carrito
            if (cartProduct.getStock() <= 0 || cartProduct.getStock() < cart.getQuantity() ) {
                cartRepository.delete(cart);
                continue;
            }

            //Se descuentan los productos del stock
            cartProduct.setStock( cartProduct.getStock() - cart.getQuantity() );
            productRepository.save(cartProduct);

            CartItemDto cartItemDto = new CartItemDto(cart.getId(), cart.getQuantity(), cartProduct);
            cartItems.add(cartItemDto);
        }

        double totalCost = 0;
        for (CartItemDto cartItemDto: cartItems) {
            totalCost += (cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity());
        }

        CartDto cartDto = new CartDto(cartItems, totalCost);
        List<CartItemDto> cartItemDtoList = cartDto.getCartItems();

        //Ahora creamos el pedido y lo guardamos
        Orders newOrder = new Orders();
        newOrder.setCreatedDate(new Date());
        newOrder.setPaymentToken(paymentToken);
        newOrder.setUsername(username);
        newOrder.setTotalPrice(cartDto.getTotalCost());
        newOrder.setPayed(false);

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (CartItemDto cartItemDto:cartItemDtoList) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setCreatedDate(new Date());
            orderProduct.setPrice(cartItemDto.getProduct().getPrice());
            orderProduct.setProduct(cartItemDto.getProduct());
            orderProduct.setQuantity(cartItemDto.getQuantity());
            orderProduct.setOrderId(newOrder.getOrderId());
            orderProducts.add(orderProduct);
        }
        newOrder.setOrderProducts(orderProducts);
        ordersRepository.save(newOrder);
        return new ResponseEntity<>(new ApiResponse(true, "El pedido se realizó exitosamente"), HttpStatus.CREATED);
    }

    @GetMapping("/order/getAllOrdersByUsername/{username}")
    public ResponseEntity<List<Orders>> getAllOrdersByUsername(@PathVariable String username) {

        List<Orders> ordersList = ordersRepository.findAllByUsernameOrderByCreatedDateDesc(username);

        return new ResponseEntity<>(ordersList, HttpStatus.OK);
    }

    @GetMapping("/order/getOrderByOrderId/{orderId}")
    public Orders getOrderById(@PathVariable String orderId, @RequestParam String username) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No se encontró un pedido con el ID: " + orderId));
        if (!order.getUsername().equals(username))
            throw new NotAuthorizedException("Usted no está autorizado para realizar esta accion");
        return order;
    }

    @GetMapping("/order/getAllOrdersNotPayed/")
    public List<Orders> getAllOrdersNotPayed() {
        Query query = new Query();
        query.addCriteria(Criteria.where("isPayed").is(false));
        List<Orders> ordersNotPayed = mongoTemplate.find(query, Orders.class);
        return ordersNotPayed;
    }

    @PutMapping("/order/confirmOrderIsPayed/")
    public ResponseEntity<ApiResponse> confirmOrderIsPayed(@RequestBody OrderIdUsernameDto orderIdUsernameDto) {
        Orders order = ordersRepository.findById(orderIdUsernameDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("No se encontró un pedido con el ID: " + orderIdUsernameDto.getOrderId()));

        if (!order.getUsername().equals(orderIdUsernameDto.getUsername()))
            throw new NotAuthorizedException("Usted no está autorizado para realizar esta accion");

        order.setPayed(true);

        //Se suma la cantidad de productos comprados al contador de ventas del producto
        List<OrderProduct> orderProducts = order.getOrderProducts();
        for (OrderProduct orderProduct: orderProducts) {
            Product product = productRepository.findById(orderProduct.getProduct().getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("No se encontró un producto con el ID: " + orderProduct.getProduct().getProductId()));;
            product.setSales( product.getSales() + orderProduct.getQuantity() );
            productRepository.save(product);
        }
        ordersRepository.save(order);
        //Limpiamos el carrito de compras
        cartRepository.deleteByUsername(orderIdUsernameDto.getUsername());
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Se confirmó el pago del pedido exitosamente"), HttpStatus.OK);
    }

    @DeleteMapping("/order/deleteOrderDeclined/")
    public ResponseEntity<ApiResponse> deleteOrderDeclined(@RequestBody OrderIdUsernameDto orderIdUsernameDto) {
        Orders order = ordersRepository.findById(orderIdUsernameDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("No se encontró un pedido con el ID: " + orderIdUsernameDto.getOrderId()));

        if (!orderIdUsernameDto.getUsername().equals(order.getUsername()))
            throw new NotAuthorizedException("Usted no está autorizado para realizar esta accion");

        if (order.isPayed() == true)
            throw new NotAuthorizedException("Este pedido no se puede eliminar debido a que ya fue pagado");

        //Se devuelven al stock los productos, debido a que no fueron comprados
        List<OrderProduct> orderProducts = order.getOrderProducts();
        for (OrderProduct orderProduct: orderProducts) {
            Product product = productRepository.findById(orderProduct.getProduct().getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("No se encontró un producto con el ID: " + orderProduct.getProduct().getProductId()));;
            product.setStock( product.getStock() + orderProduct.getQuantity() );
            productRepository.save(product);
        }

        ordersRepository.delete(order);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "El pedido fue eliminado exitosamente"), HttpStatus.OK);
    }
}