const { gql } = require('apollo-server');

const productTypeDefs = gql `
    type Product {
        productId: String!
        productName: String!
        imageURL: String!
        price: double!
        stock: Int!
        description: String!
        category: String!
    }
    
    extend type Query {
        productByProductId(productId: String!): Product
    }
`;

module.exports = productTypeDefs;