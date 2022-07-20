const { gql } = require('apollo-server');

const inventoryTypeDefs = gql `
    type Inventory {
        id: String!
        productId: String!
        stock: Int!
        movement: String!
        modifyDate: String!
    }
    input InventorySale {
        productId: String!
        stock: Int!
    }
    input InventoryAdd {
        productId: String!
        stock: Int!
    }
    extend type Query {
        inventoryByProducId(productId: String!): [Inventory]!
    }  
    extend type Mutation {
        createInventorySale(inventory: InventorySale!): Inventory!
        createInventoryAdd(inventory: InventoryAdd!): Inventory!
    }
`;
    //por qué tiene llaves y el de abajo no

module.exports = inventoryTypeDefs;