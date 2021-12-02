const { gql } = require('apollo-server');

const productTypeDefs = gql `
    type Product {
        productId: String!
        productName: String!
        imageURL: String!
        price: Float!
        stock: Int!
        description: String!
        category: String!
    }

    input ProductInput {
        productId: String!
        productName: String!
        imageURL: String!
        price: Float!
        stock: Int!
        description: String!
        category: String!
    }

    extend type Query {
        productByproductId(productId: String!): Product
    }

    extend type Mutation {
        createProduct(product: ProductInput!): Product
    }
`;

module.exports = productTypeDefs;