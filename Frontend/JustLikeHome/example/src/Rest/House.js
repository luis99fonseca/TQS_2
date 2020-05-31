import { Component } from 'react';
import {  url } from "./Const";

export default class House extends Component{

    constructor(props) {
        super(props);
        
    }

    async post_house(data) {
        const response = await fetch(url + '/newHouse', {
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


    async addComodities(data) {
        const response = await fetch(url + '/addComoditie', {
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

    async get_userHouse() {
    
        const response = await fetch(url + '/userHouses/user=' + localStorage.getItem('user_id') , {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
                //'Authorization': 'Token ' + localStorage.getItem('token')
            },
        })

        const status = await response.status
        const json = await response.json()
        return [status, json]
    }

    async get_specificHouse() {
        const response = await fetch(url + '/specificHouse/houseId=' + localStorage.getItem('house_id') , {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
                //'Authorization': 'Token ' + localStorage.getItem('token')
            },
        })

        const status = await response.status
        const json = await response.json()
        return [status, json]
    }

    async get_topHouses() {
        const response = await fetch(url + '/topHouses' , {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
                //'Authorization': 'Token ' + localStorage.getItem('token')
            },
        })

        const status = await response.status
        const json = await response.json()
        return [status, json]
    }

    
    

    async get_reviews() {
        const response = await fetch(url + '/houseReviews/house=' + localStorage.getItem('house_id'), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
                //'Authorization': 'Token ' + localStorage.getItem('token')
            },
        })

        const status = await response.status
        const json = await response.json()
        return [status, json]
    }

    async makeReview_house(data){
        const response = await fetch(url + '/newHouseReview', {
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

    async updateHouse(data){
        const response = await fetch(url + '/updateHouse', {
            method: 'PUT',
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