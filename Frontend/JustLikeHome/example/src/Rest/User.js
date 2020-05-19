import { Component } from 'react';
import {  url } from "./Const";

export default class User extends Component{

    constructor(props) {
        super(props);
        
    }

   async create_user(data){
    const response = await fetch(url + '/createUser', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
            //'Authorization': 'Token ' + localStorage.getItem('token')
        },
        body: JSON.stringify(data)
    })

    const status = await response.status
    const json = await response.json()
    return [status, json]
   }


   async rent_house(data){
    const response = await fetch(url + '/askToRent', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
            //'Authorization': 'Token ' + localStorage.getItem('token')
        },
        body: JSON.stringify(data)
    })

    const status = await response.status
    const json = await response.json()
    return [status, json]
   }
}