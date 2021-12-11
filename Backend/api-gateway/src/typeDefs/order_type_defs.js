const { gql } = require('apollo-server');

const orderTypeDefs = gql `
    type MutationOrderResponse {
        success: Boolean!
        message: String!
        timestamp: String!
    }

    type Product {
        productId: String!
        productName: String!
        imageURL: String!
        price: Float!
        stock: Int!
        description: String!
        category: String!
        sales: Int!
    }

    type OrderProduct {
        product: Product!
        price: Float!
        quantity: Int!
        createdDate: String!
    }

    type Order {
        orderId: String!
        username: String!
        paymentToken: String!
        createdDate: String!
        totalPrice: Float!
        orderProducts: [OrderProduct!]!
    }

    input CreateOrderInput {
        username: String!
        paymentToken: String!
    }

    input GetOrderByIdInput {
        orderId: String!
        username: String!
    }

    extend type Mutation {
        createOrder(createOrderInput: CreateOrderInput!): MutationOrderResponse
    }

    extend type Query {
        getOrderById(orderByIdInput: GetOrderByIdInput): Order
        getAllOrdersByUsername(username: String!): [Order!]
    }
`;

module.exports = orderTypeDefs;