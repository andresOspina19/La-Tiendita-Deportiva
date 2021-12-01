const { gql } = require('apollo-server');

const inventoryTypeDefs = gql `
    type Inventory {
        id: String!
        productId: String!
        stock: Int!
        movement: String!
        modifyDate: String!
    }
    
    input InventoryInput {
        productId: String!
        stock: Int!
    }

    extend type Query {
        inventoryByProducId(productId: String!): [Inventory]
    }
    
    extend type Mutation {
        createInventory(inventory: InventoryInput!): Inventory
    }
`;
    //por qué tiene llaves y el de abajo no

module.exports = inventoryTypeDefs;