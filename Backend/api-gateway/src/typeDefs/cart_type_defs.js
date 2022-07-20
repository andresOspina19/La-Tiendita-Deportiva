const { gql } = require('apollo-server');

const cartTypeDefs = gql `
    type MutationCartItemResponse {
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

    type CartItem {
        id: String!
        quantity: Int!
        product: Product!
    }

    type Cart {
        cartItems: [CartItem!]!
        totalCost: Float!
    }

    input AddToCartInput {
        username: String!
        productId: String!
        quantity: Int!
    }

    input UpdateItemInput {
        id: String!
        productId: String!
        quantity: Int!
        username: String!
    }

    input DeleteItemInput {
        cartItemId: String!
        username: String!
    }

    input GetCartInput {
        username: String!
    }

    extend type Mutation {
        addCartItem(addToCartInput :AddToCartInput): MutationCartItemResponse
        updateCartItem(updateCartItem: UpdateItemInput!): MutationCartItemResponse
        deleteCartItem(deleteItemInput: DeleteItemInput): MutationCartItemResponse
    }

    extend type Query {
        getUserCart(username: String!): Cart
    }
`;

module.exports = cartTypeDefs;