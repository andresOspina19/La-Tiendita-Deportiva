const { gql } = require('apollo-server');

const payTypeDefs = gql `
    type Paymenttoken {
        token: String!
        username: String!
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
    type RedirectUrl {
        redirectUrl: String!
        transactionReference: String!
    }
    input AmountInput {
        subtotalIva: Float!
        subtotalIva0: Float!
        iva: Float!
    }
    input Payment {
        token: String!
        username: String!
        amount: AmountInput!
    }

    input PaymentByTokenInput {
        username: String!
        token: String!
    }

    extend type Query {
        getPaymentByToken(paymentByTokenInput: PaymentByTokenInput!): Paymenttoken
    }
    extend type Mutation {
        initPayment(payment: Payment!): RedirectUrl!
    }
`;

module.exports = payTypeDefs;