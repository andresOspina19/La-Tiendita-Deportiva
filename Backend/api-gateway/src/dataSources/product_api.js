const { RESTDataSource } = require('apollo-datasource-rest');

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