const { gql } = require('apollo-server');

const productTypeDefs = gql `
    type Product {
        productId: String!
        productName: String!
        imageURL: String
        price: Float!
        stock: Int!
        description: String!
        category: String!
        sales: Int!
    }

    type paginationProducts {
        totalItems: Int!
        totalPages: Int!
        currentPage: Int!
        products: [Product]!
    }

    input paginationProductsInput {
        productName: String
        page: Int
        size: Int
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
        productsByNameOrWithoutNameOrderedBySales(pagination: paginationProductsInput): paginationProducts! 
    }

    extend type Mutation {
        createProduct(product: ProductInput!): Product
    }
`;

module.exports = productTypeDefs;