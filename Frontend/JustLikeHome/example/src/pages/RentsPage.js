import React, { Component, useCallback } from "react";
import {  url } from "../Rest/Const";
import SiteWrapper from "../SiteWrapper.react";
import { Page, Grid, Form, Table, Card, Icon, Button } from "tabler-react";

import User from "../Rest/User"
import getDataForm from "../Rest/getDataForm";

export default class RentsPage extends Component {


    constructor(props) {
        super(props);
        
        this.state = {
            rents_requests : [],
            onGoing_rents : []
          }

          this.user_obj = new User()
          //this.get_rentsRequests()
      //    this.get_onGoingRequests()
    }


    //async get_rentsRequests(){
//
    //}
}