import React from 'react';
import {Link, useRouteError} from 'react-router-dom';
import Button from "react-bootstrap/Button";

const ErrorPage = () => {
    const error = useRouteError();
    console.error("error");

    return (
    <div className="container-xxl py-6 wow fadeInUp">
        <div className="container text-center">
            <div className="row justify-content-center">
                <div className="col-lg-6 text-color-grey">
                    <i className="bi bi-exclamation-triangle display-1 primary"></i>
                    <h1 className="display-1" style={{"fontWeight": "700"}}>Something went wrong...</h1>
                    <p className="mt-4 mb-2 text-color-grey">We’re sorry, an unexpected error has occurred. The page you
                        have looked for does not exist.</p>
                    <p className="mt-2 mb-4  text-color-grey">Error status: {error.statusText || error.message}</p>
                    <Button variant="primary" type="submit"
                            className="py-3 px-5 justify-content-evenly lg-signup-submit" onClick={() => {
                        window.location.href = "/"
                    }}>
                        Go to Home
                    </Button>
                </div>
            </div>
        </div>
    </div>
    )
        ;
};

export default ErrorPage;
