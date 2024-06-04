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
                        src="/logo512.png"
                        alt="First slide"
                        width="500" height="600"
                    />
                    <Carousel.Caption>
                        <div className={"container"}>
                            <div className={"row justify-content-center"}>
                                <div className={"col-lg-7"}>
                                    <h1 className={"display-2 text-light mb-5 animate__bounce"}>Інструктори-експерти</h1>

                                    <Link to="/courses" className={"btn btn-light py-sm-3 px-sm-5 ms-3"}>Наші курси</Link>
                                </div>
                            </div>
                        </div>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item interval={5000}>
                    <img
                        className="d-block w-100"
                        src="/logo512.png"
                        alt="Second slide"
                        width="500" height="600"
                    />
                    <Carousel.Caption>
                        <div className={"container"}>
                            <div className={"row justify-content-center"}>
                                <div className={"col-lg-7"}>
                                    <h1 className={"display-2 text-light mb-5 slideInDown animate__slideInDown"}>Комплексний навчальний план</h1>

                                    <Link to="/courses" className={"btn btn-light no-radius py-sm-3 px-sm-5 ms-3"}>Наші курси</Link>
                                </div>
                            </div>
                        </div>
                    </Carousel.Caption>
                </Carousel.Item>
                {/*<Carousel.Item interval={5000}>*/}
                {/*    <img*/}
                {/*        className="d-block w-100"*/}
                {/*        src="/logo512.jpg"*/}
                {/*        alt="Third slide"*/}
                {/*    />*/}
                {/*    <Carousel.Caption>*/}
                {/*        <div className={"container"}>*/}
                {/*            <div className={"row justify-content-center"}>*/}
                {/*                <div className={"col-lg-7"}>*/}
                {/*                    <h1 className={"display-2 text-light mb-5"}>Підготовка до тестування</h1>*/}

                {/*                    <Link to="/courses" className={"btn btn-light no-radius py-sm-3 px-sm-5 ms-3"}>Наші курси</Link>*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}
                {/*    </Carousel.Caption>*/}
                {/*</Carousel.Item>*/}
            </Carousel>
        </div>
    );
}