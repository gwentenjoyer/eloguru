import React from "react";
import { Link } from "react-router-dom";
import "../../css/Footer.css";
import "bootstrap/dist/css/bootstrap.min.css";

export default function Footer() {
    return (
        <footer className="container-fluid navbar-blu text-light footer mb-0 py-6 wow fadeIn">
            <div className="container">
                <div className="row g-5">
                    <div className="col-lg-4 col-md-6">
                        <h4 className="text-white mb-4 font-6">Knowledge is power.</h4>
                        <h2 className="primary mb-4 font-6 "><i className="fa fa-solid fa-user-graduate text-white me-2"></i>Eloguru</h2>
                        <h5 className="primary mb-4 font-6 text-white">Support:</h5>
                        <p className="mb-2"><i className="fa fa-envelope me-3"></i>eloguruedu@gmail.com</p>
                        <p className="mb-2"><i className="fa fa-phone-alt me-3"></i>+380 68 420 13 37</p>
                    </div>
                    <div className="col-lg-4 col-md-6">
                        <h4 className="text-light mb-4 font-6">Popular links</h4>
                        <Link className="btn btn-link" to="/about">About</Link>
                        <Link className="btn btn-link" to="/courses">Courses</Link>
                    </div>
                </div>
            </div>
        </footer>
    );
}
