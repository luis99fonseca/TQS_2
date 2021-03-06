// @flow

import * as React from "react";
import { NavLink, withRouter } from "react-router-dom";
import Icon from "./components/Icon";
import {
  Site,
  Nav,
  Grid,
  List,
  Button,
  RouterContextProvider,
} from "tabler-react";

import type { NotificationProps } from "tabler-react";

type Props = {|
  +children: React.Node,
|};

type State = {|
  notificationsObjects: Array<NotificationProps>,
|};

type subNavItem = {|
  +value: string,
  +to?: string,
  +icon?: string,
  +LinkComponent?: React.ElementType,
  +useExact?: boolean,
|};

type navItem = {|
  +value: string,
  +to?: string,
  +icon?: string,
  +active?: boolean,
  +LinkComponent?: React.ElementType,
  +subItems?: Array<subNavItem>,
  +useExact?: boolean,
|};

const navBarItems: Array<navItem> = (localStorage.getItem("username")=== "" || localStorage.getItem('username') === null) ? 
  [
    {
      value: "Página Inicial",
      to: "/",
      icon: "square",
      LinkComponent: withRouter(NavLink),
      useExact: true,
    },
    {
      value: "Imóveis",
      to: "/imoveis",
      icon: "home",
      LinkComponent: withRouter(NavLink),
    },
  ] 
  : [
  {
    value: "Página Inicial",
    to: "/",
    icon: "square",
    LinkComponent: withRouter(NavLink),
    useExact: true,
  },
  {
    value: "Imóveis",
    to: "/imoveis",
    icon: "home",
    LinkComponent: withRouter(NavLink),
  },
  {
    value: "Seus anúncios",
    to: "/announcements",
    icon: "briefcase",
    LinkComponent: withRouter(NavLink),
  },
  {
    value: "Arrendamentos",
    to: "/rents",
    icon: "check-circle",
    LinkComponent: withRouter(NavLink),
  },
  {
    value: "Profile",
    icon: "user",
    to: "/profile",
    LinkComponent: withRouter(NavLink)
       
  }
];

const accountDropdownProps = {
  avatarURL: "/demo/faces/female/25.jpg",
  name: (localStorage.getItem('username') === "" || localStorage.getItem('username') === null) ? "Visitante" : localStorage.getItem('username'),
  options: (localStorage.getItem('username') === "" || localStorage.getItem('username') === null) ? [{ icon: "log-in", value: "Login", to: "/login" }] :[
    { icon: "user", value: "Profile", to: "/profile" },
    { icon: "log-out", value: "Sign out", to: "/logout"},] ,
};

class SiteWrapper extends React.Component<Props, State> {
  state = {
    notificationsObjects: [
      {
        unread: true,
        avatarURL: "demo/faces/male/41.jpg",
        message: (
          <React.Fragment>
            <strong>Nathan</strong> pushed new commit: Fix page load performance
            issue.
          </React.Fragment>
        ),
        time: "10 minutes ago",
      },
      {
        unread: true,
        avatarURL: "demo/faces/female/1.jpg",
        message: (
          <React.Fragment>
            <strong>Alice</strong> started new task: Tabler UI design.
          </React.Fragment>
        ),
        time: "1 hour ago",
      },
      {
        unread: false,
        avatarURL: "demo/faces/female/18.jpg",
        message: (
          <React.Fragment>
            <strong>Rose</strong> deployed new version of NodeJS REST Api // V3
          </React.Fragment>
        ),
        time: "2 hours ago",
      },
    ],
  };

  render(): React.Node {
    const notificationsObjects = this.state.notificationsObjects || [];
    const unreadCount = this.state.notificationsObjects.reduce(
      (a, v) => a || v.unread,
      false
    );
    return (
      <Site.Wrapper
        headerProps={{
          href: "/",
          alt: "Just Like Home",
          imageURL: "./demo/logojhl.png",
          accountDropdown: accountDropdownProps,
        }}
        navProps={{ itemsObjects: navBarItems }}
        routerContextComponentType={withRouter(RouterContextProvider)}
        footerProps={{
          copyright: (
            <React.Fragment>
              Copyright © 2019
              All rights reserved.
            </React.Fragment>
          ),
          nav: (
            <React.Fragment>
              <Grid.Col auto={true}>
                <List className="list-inline list-inline-dots mb-0">
                  <List.Item className="list-inline-item">
                    <a href="./docs/index.html">Documentation</a>
                  </List.Item>
                  <List.Item className="list-inline-item">
                    <a href="./faq.html">FAQ</a>
                  </List.Item>
                </List>
              </Grid.Col>
              <Grid.Col auto={true}>
                <Button
                  href="https://github.com/MotaMiguel/TQS_2"
                  size="sm"
                  outline
                  color="primary"
                  RootComponent="a"
                >
                  Source code
                </Button>
              </Grid.Col>
            </React.Fragment>
          ),
        }}
      >
        {this.props.children}
      </Site.Wrapper>
    );
  }
}

export default SiteWrapper;
