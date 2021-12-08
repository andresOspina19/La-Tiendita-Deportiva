const { RESTDataSource } = require('apollo-datasource-rest');

const serverConfig = require('../server');


class PayAPI extends RESTDataSource {

    constructor() {
        super();
        this.baseURL = serverConfig.pay_api_url;
    }

    async getPaymentByToken(token) {
        return await this.get(`/getPaymentByToken/${token}`);
    }

    async getAllPaymentsByUsername(username) {
        return await this.get(`/getAllPaymentsByUsername/${username}`);
    }

    async getOnlyApprovedPaymentsByUsername(username) {
        return await this.get(`/getOnlyApprovedPaymentsByUsername/${username}`);
    }
    
    async initPayment(payment) {
        payment = new Object(JSON.parse(JSON.stringify(payment)));
        return await this.post('/initPayment', payment);
    }
}

module.exports = PayAPI;