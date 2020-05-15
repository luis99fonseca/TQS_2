//@flow

import * as React from "react";
import Card from "../Card/Card.react";
import Icon from "../Icon/Icon.react";

type Props = {|
  +children?: React.Node,
  +title?: string,
  +titleUrl?: string,
  +avatarUrl?: string,
  +description?: string,
  +date?: string,
  +imgUrl?: string,
  +iconName?: string,
  +iconHref?: string,
  +authorName?: string,
  +avatarImgSrc?: string,
  +imgSrc?: string,
  +imgAlt?: string,
  +aside?: boolean,
  +postHref?: string,
  +profileHref?: string,
|};

function BlogCard({
  children,
  title,
  description,
  avatarUrl,
  imgUrl,
  imgAlt,
  aside,
  authorName,
  date,
  imgSrc = "",

  postHref,
  profileHref,
}: Props): React.Node {
  return !aside ? (
    <Card>
      <a href={postHref}>
        <img className="card-img-top" src={imgSrc} alt={imgAlt} />
      </a>
      <Card.Body className="d-flex flex-column">
        <h4>
          <a href={postHref}>{title}</a>
        </h4>
        <div className="text-muted">{description}</div>
      </Card.Body>
    </Card>
  ) : (
    <Card className="card-aside">
      <a
        href={postHref}
        className="card-aside-column"
        style={{ backgroundImage: `url(${imgSrc})` }}
      >
        {""}
      </a>
      <Card.Body className="d-flex flex-column">
        <h4>
          <a href={postHref}>{title}</a>
        </h4>
        <div className="text-muted">{description}</div>
      </Card.Body>
    </Card>
  );
}

/** @component */
export default BlogCard;
