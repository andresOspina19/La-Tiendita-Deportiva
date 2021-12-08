const { gql } = require('apollo-server');

const payTypeDefs = gql `
    type Paymenttoken {
        token: String!
        username: String!
        purchaseId: String!
        amount: Amount!
        status: String!
        bankurl: String!
        date: String!
    }
    type Amount {
        subtotalIva: Float!
        subtotalIva0: Float!
        iva: Float!
    }
    type Transaccion{
        redirectUrl: String!
        transactionReference: String!
    }
    input Payment {
        token: String!
        username: String!
        purchaseId: String!
        amount: Amountpay!
    }
    input Amountpay {
        subtotalIva: Float!
        subtotalIva0: Float!
        iva: Float!
    }
    extend type Query {
        getPaymentByToken(token: String!): Paymenttoken
        getAllPaymentsByUsername(username: String!): [Paymenttoken]
        getOnlyApprovedPaymentsByUsername(username: String!): [Paymenttoken]
    }
    extend type Mutation {
        initPayment(transaccion: Payment!): Transaccion!
    }
`;

module.exports = payTypeDefs;