import React from "react";
import "../../css/Footer.css"
import "bootstrap/dist/css/bootstrap.min.css"
import {Link} from "react-router-dom";

export default function Footer() {
    return (
        <footer className="container-fluid bg-dark text-light footer mb-0 py-6 wow fadeIn">
            <div className="container">
                <div className="row g-5">
                    <div className="col-lg-4 col-md-6">
                        <h4 className="text-white mb-4 font-6">Будемо на зв'язку</h4>
                        <h2 className="primary mb-4 font-6"><i className="fa fa-car text-white me-2"></i>Carbon</h2>
                        <p className="mb-2"><i className="fa fa-map-marker-alt me-3"></i>Тернопіль, вул. М. Леонтовича 32</p>
                        <p className="mb-2"><i className="fa fa-phone-alt me-3"></i>+380 98 102 70 70</p>
                        <p className="mb-2"><i className="fa fa-envelope me-3"></i>carbondrivingschool@gmail.com</p>
                    </div>
                    <div className="col-lg-4 col-md-6">
                        <h4 className="text-light mb-4 font-6">Популярні посилання</h4>
                        <Link className="btn btn-link" to="/about">Про нас</Link>
                        <Link className="btn btn-link" to="/quiz">Тести</Link>
                        <Link className="btn btn-link" to="/courses">Курси</Link>
                        <Link className="btn btn-link" to="/teachers">Викладачі</Link>
                    </div>
                </div>
            </div>
        </footer>
    );
}