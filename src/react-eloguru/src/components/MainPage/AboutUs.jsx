import React from "react";
import "bootstrap/dist/css/bootstrap.min.css"
// import "animate.css"
import {Link} from "react-router-dom";

export default function AboutUs() {
    return (
        <div className={"container-xxl py-6 w-100"}>
            <div className={"container"}>
                <div className={"d-flex flex-row g-5 w-100"}>

                    <div className={"col-lg-6 fadeInUp"}
                         style={{"visibility": "visible", "animationDelay": "0.5s", "animationName": "fadeInUp"}}>
                        <div className={"h-100 text-color-grey"}>
                            <h6 className={"main-blu text-uppercase mb-2 font-6"}>Who are we?</h6>
                            <h1 className={"display-6 mb-4"}>We help you to help yourself to know more</h1>
                            <p>Our online course platform offers you training for people of all ages and experience levels.
                                By focusing on accessible and effective learning,
                                we create a supportive learning environment that enables our learners to gain the knowledge
                                skills and confidence they need to achieve their goals. Our courses are designed by
                                by experienced and certified teachers who bring their expertise and exceptional
                                teaching abilities to every lesson. On our platform, you have the opportunity to
                                register, choose the courses you are interested in, and study them at your own convenience. We guarantee
                                that every student can achieve the desired results.
                                Moreover, you can also become a teacher yourself on our platform, share your knowledge and experience with others
                                and experience with others, expand your audience, and influence the education of future generations. Join us
                                to our community of students and teachers, develop your skills and knowledge, and open up new opportunities
                                for personal and professional growth. Our platform is designed for you and your success.
                                Learn, teach, develop with us!
                            </p>
                            <p className="mb-4"></p>
                            <div className="row g-2 mb-4 pb-2">
                                <div className="col-sm-6">
                                    <i className="fa fa-check main-blu me-2"></i>Online access
                                </div>
                                <div className="col-sm-6">
                                    <i className="fa fa-check main-blu me-2"></i>Learn for free
                                </div>
                                <div className="col-sm-6">
                                    <i className="fa fa-check main-blu me-2"></i>Free to use
                                </div>
                                <div className="col-sm-6">
                                    <i className="fa fa-check main-blu me-2"></i>Licensed
                                </div>
                            </div>
                            <div className="d-flex justify-content-center g-4">
                                {/*<div className="col-sm-6">*/}
                                    <Link className="btn btn-warning btn-main-blu py-3 px-5" to="/about">Читати більше</Link>
                                {/*</div>*/}
                            </div>
                        </div>
                    </div>
                <div className={"col-lg-6 wow fadeInUp"}
                     style={{"visibility": "visible", "animationDelay": "0.1s", "animationName": "fadeInUp"}}>
                    <div className={"position-relative overflow-hidden ps-5 pt-5 h-100"}
                         style={{"minHeight": "400px"}}>
                        <img className={"position-absolute w-100 h-100"} src={"/aboutimg.jpg"}
                             style={{"objectFit": "cover"}} alt={""}/>
                    </div>
                </div>
                </div>
            </div>
        </div>
    );
}