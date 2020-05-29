
import React, { Component, useCallback } from "react";
import { Page, Grid, GalleryCard, Form, Button, Container, Text, Card} from "tabler-react";
import SiteWrapper from "./SiteWrapper.react";

export default  class Logout extends Component {
    
    constructor(props) {
        super(props);

        this.logout_action()

    }

    logout_action(){
        localStorage.setItem("user_id","")
        localStorage.setItem("username", "")
        window.location.href = '/login'  
    }

    render(){
        return(
            <SiteWrapper>
                
            </SiteWrapper>
        )
    }
}