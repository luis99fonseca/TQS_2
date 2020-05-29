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


   async askRent_house(data){
    
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


   async get_rents_requests(data){
    const response = await fetch(url + '/pendingRents/user=' + localStorage.getItem('user_id'), {
        method: 'GET',
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

   async get_rents_ongoing(data){
    
    const response = await fetch(url + '/onGoingRents/user=' + localStorage.getItem('user_id'), {
        method: 'GET',
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

   async accept_rents(data){
    const response = await fetch(url + '/acceptRent', {
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

   async deny_rents(data){
    const response = await fetch(url + '/denyRent', {
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

   async get_user_reviews(id){
    
    const response = await fetch(url + '/userReviews/user=' +id, {
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

   async makeReview_otherUser(data){
    const response = await fetch(url + '/newUserReview', {
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

   async addHouseToBookmarker(data){
    
    const response = await fetch(url + '/addBookmark', {
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

   async deleteHouseFromBookmarker(id){
       // later use cache for dynamic id user
    const response = await fetch(url + '/deleteBookmark/userId='+ localStorage.getItem('user_id') + '&houseId=' + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
            //'Authorization': 'Token ' + localStorage.getItem('token')
        },
    })

    const status = await response.status
    const json = await response.json()
    return [status, json]
  
   }


   async getInfoUser(){
     const response = await fetch(url + '/userInfo/user='+ localStorage.getItem('user_id'), {
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

    async getOtherUser(id){
       
         const response = await fetch(url + '/userInfo/user='+id, {
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

    async deleteBookMarker(houseId){
       
     const response = await fetch(url + '/deleteBookmark/userId='+ localStorage.getItem('user_id') +'&houseId=' + houseId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
            //'Authorization': 'Token ' + localStorage.getItem('token')
        },
    })

    const status = await response.status
    const json = await response.json()
    return [status, json]
    }


    async loginUser(data){
         const response = await fetch(url + '/login', {
             method: 'GET',
             headers: {
                 'Content-Type': 'application/json',
                 'username' :  data['username'],
                 'password': data['password']
                 
             },
         })
    
         const status = await response.status
         const json = await response.json()
         return [status, json]
    
        }


}