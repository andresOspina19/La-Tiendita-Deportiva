package com.latienditadeportiva.Inventory_ms.controllers;

import com.latienditadeportiva.Inventory_ms.dto.cart.AddToCartDto;
import com.latienditadeportiva.Inventory_ms.dto.cart.CartDto;
import com.latienditadeportiva.Inventory_ms.dto.cart.CartItemDto;
import com.latienditadeportiva.Inventory_ms.exceptions.CartItemNotExistException;
import com.latienditadeportiva.Inventory_ms.exceptions.NoStockException;
import com.latienditadeportiva.Inventory_ms.exceptions.ProductNotFoundException;
import com.latienditadeportiva.Inventory_ms.models.Cart;
import com.latienditadeportiva.Inventory_ms.models.Product;
import com.latienditadeportiva.Inventory_ms.repositories.CartRepository;
import com.latienditadeportiva.Inventory_ms.repositories.ProductRepository;
import com.latienditadeportiva.Inventory_ms.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/cart/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody Cart cartItem) {
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("No se encontró un producto con el ID: " + cartItem.getProductId()));

        if (product.getStock() < cartItem.getQuantity())
            throw new NoStockException("No hay unidades en el inventario suficientes");
        if (cartItem.getQuantity() <= 0)
            throw new NoStockException("La operacion no es valida");

        cartRepository.save(cartItem);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "¡Añadido al carrito!"), HttpStatus.CREATED);
    }

    @GetMapping("/cart/getItemsByUsername/{username}")
    public ResponseEntity<CartDto> getCartItems(@PathVariable String username) {
        List<Cart> cartList = cartRepository.findAllByUsernameOrderByCreatedDateDesc(username);
        List<CartItemDto> cartItems = new ArrayList<>();

        for (Cart cart:cartList) {
            Product cartproduct = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("No se encontró un producto con el ID: " + cart.getProductId()));

            //Verificamos que existan unidades del producto, sino, lo eliminamos automaticamente del carrito
            if (cartproduct.getStock() <= 0 || cartproduct.getStock() < cart.getQuantity() ) {
                cartRepository.delete(cart);
                continue;
            }

            CartItemDto cartItemDto = new CartItemDto(cart.getId(), cart.getQuantity(), cartproduct);
            cartItems.add(cartItemDto);
        }

        double totalCost = 0;
        for (CartItemDto cartItemDto: cartItems) {
            totalCost += (cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity());
        }

        CartDto cartDto = new CartDto(cartItems, totalCost);

        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }

    @PutMapping("/cart/updateItem")
    public ResponseEntity<ApiResponse> updateCartItem(@RequestBody @Valid AddToCartDto cartDto) {
        Product product = productRepository.findById(cartDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("No se encontró un producto con el ID: " + cartDto.getProductId()));

        if (product.getStock() < cartDto.getQuantity())
            throw new NoStockException("No hay unidades en el inventario suficientes");
        if (cartDto.getQuantity() <= 0)
            throw new NoStockException("La operacion no es valida");

        Cart cart = cartRepository.findById(cartDto.getId())
                .orElseThrow(() -> new CartItemNotExistException("No se encontró ese producto en el carrito"));

        cart.setQuantity(cartDto.getQuantity());
        cart.setCreatedDate(new Date());
        cartRepository.save(cart);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "El carrito se ha actualizado exitosamente"), HttpStatus.OK);
    }

    @DeleteMapping("/cart/deleteItemById/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable String cartItemId) {
        if (!cartRepository.existsById(cartItemId))
            throw new CartItemNotExistException("No se encontró un producto en el carrito con id " + cartItemId);

        cartRepository.deleteById(cartItemId);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "El producto fue eliminado del carrito"), HttpStatus.OK);
    }
}
