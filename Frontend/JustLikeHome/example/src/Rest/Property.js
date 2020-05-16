import { Component } from 'react';
import {  url } from "./Const";

export default class Property extends Component{

    constructor(props) {
        super(props);
        
    }


    async get_searchProperty(city, startDate, endDate, guests) {
        console.log(url + '/houses/city=' + city + '&start=' + startDate + '&end=' + endDate + '&guests=' +guests)
        const response = await fetch(url + '/houses/city=' + city + '&start=' + startDate + '&end=' + endDate + '&guests=' +guests, {
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

}