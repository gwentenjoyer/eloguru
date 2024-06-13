import React from "react";
import {Carousel} from "react-bootstrap";
import "../../css/slider.css";
// import "animate.css"
import {Link} from "react-router-dom";

// TODO: зробити анімацію тексту.

export default function Slider(){
    return (
        <div className={"container-fluid p-0 fadeIn"}>
            <Carousel pause={false} indicators={false}>
                <Carousel.Item interval={5000}>
                    <img
                        className="d-block w-100"
                        src="/slider1.jpg"
                        alt="First slide"
                        width="500" height="auto"
                    />
                    <Carousel.Caption>
                        <div className={"container"}>
                            <div className={"row justify-content-center"}>
                                <div className={"col-lg-7"}>
                                    <h1 className={"display-2 text-light mb-5 animate__bounce"}>Learn new things</h1>

                                    <Link to="/courses" className={"btn btn-light no-radius py-sm-3 px-sm-5 ms-3 bg-primary-yellow"}>Go to courses</Link>
                                </div>
                            </div>
                        </div>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item interval={5000}>
                    <img
                        className="d-block w-100 img-responsive"
                        src="/slider2.jpg"
                        alt="Second slide"
                        width="500" height="auto"
                    />
                    <Carousel.Caption>
                        <div className={"container"}>
                            <div className={"row justify-content-center"}>
                                <div className={"col-lg-7"}>
                                    <h1 className={"display-2 text-light mb-5 slideInDown animate__slideInDown"}>Improve your skills</h1>

                                    <Link to="/courses" className={"btn btn-light no-radius py-sm-3 px-sm-5 ms-3 bg-primary-yellow"}>Go to courses</Link>
                                </div>
                            </div>
                        </div>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item interval={5000}>
                    <img
                        className="d-block w-100 img-responsive"
                        src="/slider3.jpg"
                        alt="Second slide"
                        width="500" height="auto"
                    />
                    <Carousel.Caption>
                        <div className={"container"}>
                            <div className={"row justify-content-center"}>
                                <div className={"col-lg-7"}>
                                    <h1 className={"display-2 text-light mb-5 slideInDown animate__slideInDown"}>Start your career right now</h1>

                                    <Link to="/courses" className={"btn btn-light no-radius py-sm-3 px-sm-5 ms-3 bg-primary-yellow"}>Go to courses</Link>
                                </div>
                            </div>
                        </div>
                    </Carousel.Caption>
                </Carousel.Item>
            </Carousel>
        </div>
    );
}