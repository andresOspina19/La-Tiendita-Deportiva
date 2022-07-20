const { RESTDataSource } = require('apollo-datasource-rest');
const { sortedUniq, update } = require('lodash');

const serverConfig = require('../server');

class InventoryAPI extends RESTDataSource {

    constructor() {
        super();
        this.baseURL = serverConfig.inventory_api_url;
    }

    //Metodos de productos
    async createProduct(product) {
        product = new Object(JSON.parse(JSON.stringify(product)));
        return await this.post('/products', product);
    }

    async productByProductId(productId) {
        return await this.get(`/products/${productId}`);
    }

    async productsByNameOrWithoutNameOrderedBySales(pagination) {
        pagination = new Object(JSON.parse(JSON.stringify(pagination)));

        let url = "https://latiendita-ms-inventory.herokuapp.com/productsByNameOrWithoutNameOrderedBySales";
        let contador = 0;

        if (pagination.productName != null) {
            url += `?productName=${pagination.productName}`;
            contador++;
        }
        if (pagination.page != null) {
            if (contador == 0) {
                url += `?page=${pagination.page}`;
                contador++;
            } else {
                url += `&page=${pagination.page}`;
            }
        }
        if (pagination.size != null) {
            if (contador == 0) {
                url += `?size=${pagination.size}`;
                contador++;
            } else {
                url += `&size=${pagination.size}`;
            }
        }
        return await this.get(url);
    }
    
    //Metodos de inventario
    async inventoryByProductId(productId) {
        return await this.get(`/inventory/${productId}`);
    }

    async createInventorySale(inventory) {
        inventory = new Object(JSON.parse(JSON.stringify(inventory)));
        return await this.post('/inventorysale', inventory);
    }
    async createInventoryAdd(inventory) {
        inventory = new Object(JSON.parse(JSON.stringify(inventory)));
        return await this.post('/inventoryadd', inventory);
    }

    //Metodos del carrito de compras
    async addProductToCart(addToCartInput) {
        addToCartInput = new Object(JSON.parse(JSON.stringify(addToCartInput)));
        return await this.post('/cart/add', addToCartInput);
    }

    async UpdateProductOfCart(updateCartItem) {
        updateCartItem = new Object(JSON.parse(JSON.stringify(updateCartItem)));
        let username = updateCartItem.username;
        delete updateCartItem.username;
        return await this.put(`/cart/updateItem?username=${username}`, updateCartItem);
    }

    async getCartByUsername(username) {   
        return await this.get(`/cart/getItemsByUsername/${username}`);
    }

    async deleteProductOfCartByCartItemId(deleteItemInput) {   
        deleteItemInput = new Object(JSON.parse(JSON.stringify(deleteItemInput)));
        return await this.delete(`/cart/deleteItemById/${deleteItemInput.cartItemId}?username=${deleteItemInput.username}`);
    }

    //Metodos de los pedidos

    async createOrderByUsername(UsernameAndPaymentToken) {
        UsernameAndPaymentToken = new Object(JSON.parse(JSON.stringify(UsernameAndPaymentToken)));
        return await this.post(`/order/addOrderByUsername/${UsernameAndPaymentToken.username}?paymentToken=${UsernameAndPaymentToken.paymentToken}`);
    }

    async ConfirmOrderIsPayed(confirmOrder) {
        confirmOrder = new Object(JSON.parse(JSON.stringify(confirmOrder)));
        return await this.put(`/order/confirmOrderIsPayed/`, confirmOrder);
    }

    async deleteOrderDeclined(orderDeclined) {
        orderDeclined = new Object(JSON.parse(JSON.stringify(orderDeclined)));
        return await this.delete(`/order/deleteOrderDeclined/`, orderDeclined);
    }

    async getOrderByOrderId(orderByIdInput) {   
        orderByIdInput = new Object(JSON.parse(JSON.stringify(orderByIdInput)));
        return await this.get(`/order/getOrderByOrderId/${orderByIdInput.orderId}?username=${orderByIdInput.username}`);
    }

    async getAllOrdersNotPayed() {   
        return await this.get(`/order/getAllOrdersNotPayed/`);
    }

    async getAllOrdersByUsername(username) {   
        return await this.get(`/order/getAllOrdersByUsername/${username}`);
    }
}

module.exports = InventoryAPI;