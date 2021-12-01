const { gql } = require('apollo-server');

const authTypeDefs = gql `
    type Tokens {
        refresh: String!
        access: String!
    }

    type Access {
        access: String!
    }

    input CredentialsInput {
        username: String!
        password: String!
    }

    input SignUpInput {
        username: String!
        password: String!
        name: String!
        email: String!
        address: String!
        phoneNumber: String!
        is_active: Boolean!
    }

    type UserDetail {
        id: Int!
        username: String!
        password: String!
        name: String!
        email: String!
        address: String!
        phoneNumber: String!
        is_active: Boolean!
    }
    
    type Mutation {
        signUpUser(userInput :SignUpInput): Tokens!
        logIn(credentials: CredentialsInput!): Tokens!
        refreshToken(refresh: String!): Access!
    }

    type Query {
        userDetailById(userId: Int!): UserDetail!
    }
`;

    //preguntar por qué aparece el password acá

module.exports = authTypeDefs;