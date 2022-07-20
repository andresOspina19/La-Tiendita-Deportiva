const { invertBy } = require("lodash");

const payResolver = {
    Query: {
        getPaymentByToken: async(_, { paymentByTokenInput }, { dataSources, userIdToken }) => {
            usernameToken = (await dataSources.authAPI.getUser(userIdToken)).username;
            if (paymentByTokenInput.username == usernameToken)
                return dataSources.payAPI.getPaymentByToken(paymentByTokenInput);
            else
                return null;
        }
    },
    Mutation: {
        initPayment: async(_, { payment }, { dataSources, userIdToken }) => {
            usernameToken = (await dataSources.authAPI.getUser(userIdToken)).username;

            if (payment.username == usernameToken) {
                let redirectUrl = dataSources.payAPI.initPayment(payment);
                let UsernameAndPaymentToken = {
                    username: payment.username,
                    paymentToken: payment.token
                };
                dataSources.inventoryAPI.createOrderByUsername(UsernameAndPaymentToken);
                return redirectUrl;
            } else {
                return null;
            }
        },
        
    }
};

module.exports = payResolver;