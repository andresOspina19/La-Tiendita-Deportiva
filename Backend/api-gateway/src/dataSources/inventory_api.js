const { RESTDataSource } = require('apollo-datasource-rest');
const { sortedUniq } = require('lodash');

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
    async addProductToCart(addToCartProductDto) {
        addToCartProductDto = new Object(JSON.parse(JSON.stringify(addToCartProductDto)));
        return await this.post('/cart/add', addToCartProductDto);
    }

    async UpdateProductOfCart(updateProductOfCartDto) {
        updateProductOfCartDto = new Object(JSON.parse(JSON.stringify(updateProductOfCartDto)));
        return await this.put('/cart/updateItem', updateProductOfCartDto);
    }

    async getCartByUsername(username) {   
        return await this.get(`/cart/getItemsByUsername/${username}`);
    }

    async deleteProductOfCartByCartItemId(cartItemId) {   
        return await this.delete(`/cart/deleteByItemId/${cartItemId}`);
    }

    //Metodos de los pedidos

    async createOrderByUsername(UsernameAndPaymentToken) {
        return await this.post(`/order/addOrderByUsername/${UsernameAndPaymentToken.username}?paymentToken=${UsernameAndPaymentToken.paymentToken}`);
    }

    async getOrderByOrderId(orderId) {   
        return await this.get(`/order/getOrderByOrderId/${orderId}`);
    }

    async getAllOrdersByUsername(username) {   
        return await this.get(`/order/getAllOrdersByUsername/${username}`);
    }
}

module.exports = InventoryAPI;