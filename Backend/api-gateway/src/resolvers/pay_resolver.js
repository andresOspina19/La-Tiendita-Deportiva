const { invertBy } = require("lodash");

const payResolver = {
    Query: {
        getPaymentByToken: async(_, { token }, { dataSources }) => {
            return dataSources.payAPI.getPaymentByToken(token);
        },
        getAllPaymentsByUsername: async(_, { username }, { dataSources }) => {
            return dataSources.payAPI.getAllPaymentsByUsername(username);
        },
        getOnlyApprovedPaymentsByUsername: async(_, { username }, { dataSources }) => {
            return dataSources.payAPI.getOnlyApprovedPaymentsByUsername(username);
        }
    },
    Mutation: {
        initPayment: async(_, { transaccion }, { dataSources }) => {
            const payinput = {
                token: transaccion.token,
                username: transaccion.username,
                purchaseId: transaccion.purchaseId,
                Amount: transaccion.Amount
            }
            return await dataSources.payAPI.initPayment(transaccion);
        } 
    }
};

module.exports = payResolver;