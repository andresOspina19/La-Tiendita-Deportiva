const { RESTDataSource } = require('apollo-datasource-rest');
const { sortedUniq } = require('lodash');

const serverConfig = require('../server');

class ProductAPI extends RESTDataSource {

    constructor() {
        super();
        this.baseURL = serverConfig.inventory_api_url;
    }

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
}

module.exports = ProductAPI;