import {Fragment, useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import axios from "axios";
import {Link, useNavigate} from "react-router-dom";

import '../../css/Login.css'
export default function SignupForm() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [passwordsDismatch, setPasswordsDismatch] = useState(false);
    const [agreeMatch, setAgreeMatch] = useState(true);
    const navigate = useNavigate();
    const [selectedOption, setSelectedOption] = useState("student");
    const [hasAgreed, setHasAgreed] = useState(false);
    const [accExists, setAccExists] = useState(false);
    const [succReg, setSuccReg] = useState(false);


    const handleSubmit = (event) => {
        event.preventDefault();

        setAccExists(false);
        if (password !== confirmPassword) {
            setPasswordsDismatch(true);
            return;
        }
        else
            setPasswordsDismatch(false);
        if (hasAgreed === false) {
            setAgreeMatch(false);
            return;
        }
        else
            setAgreeMatch(true);
        const logData = {
            email,
            password
        }
        console.log(logData)
        // If passwords match, send the data to the server
        // Here you can implement your logic to send data to the server

        axios.post(`${process.env.REACT_APP_SERVER_URL}/accounts/signup?role=${selectedOption}`, logData, {withCredentials: true})
            .then(res => {
                    // window.location.href = "/profile"
                setSuccReg(true);
                    // navigate(`/profile`);
                }
            )
            .catch(e => {
                if (e.code === "ERR_BAD_REQUEST")
                    setAccExists(true);
                console.log(e)
            })
    };

    return (
        <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Email:</Form.Label>
                <Form.Control
                    // id={"lg-signup-email"}
                    // type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Enter your email"/>
            </Form.Group>

            {/*<Form.Group className="mb-3" controlId="formBasicEmail">*/}
            {/*    <Form.Label>Phone:</Form.Label>*/}
            {/*    <Form.Control*/}
            {/*        value={email}*/}
            {/*        onChange={(e) => setEmail(e.target.value)}*/}
            {/*        placeholder="Enter your email" />*/}
            {/*</Form.Group>*/}

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Password:</Form.Label>
                <Form.Control
                    type="password"
                    placeholder="Enter your password"
                    // id={"lg-signup-pw"}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </Form.Group>

            <Form.Group className="mb-4" controlId="formBasicConfirmPassword">
                <Form.Label>Confirm Password:</Form.Label>
                <Form.Control
                    type="password"
                    placeholder="Confirm your password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                />
                {passwordsDismatch && <Form.Text className="text-danger">Passwords do not match</Form.Text>}
            </Form.Group>

            {/*<Form.Group className="mb-3" controlId="formBasicCheckbox">*/}
            {/*    <Form.Check type="checkbox" label="Check me out" />*/}
            {/*</Form.Group>*/}
            <Form.Group>
            {/*    <Form.Label>Register as:</Form.Label>*/}

            {/*    <div className="d-flex flex-row mb-3">*/}
            {/*        <div className={"me-2"}>*/}

            {/*            <input*/}
            {/*                type="radio"*/}
            {/*                id="student"*/}
            {/*                name="student"*/}
            {/*                value="student"*/}
            {/*                checked={selectedOption === "student"}*/}
            {/*                onChange={() => setSelectedOption("student")}*/}
            {/*            />*/}
            {/*            <label htmlFor="student">Student</label>*/}
            {/*        </div>*/}
            {/*        <div className={"me-2"}>*/}

            {/*            <input*/}
            {/*                type="radio"*/}
            {/*                id="teacher"*/}
            {/*                name="teacher"*/}
            {/*                value="teacher"*/}
            {/*                checked={selectedOption === "teacher"}*/}
            {/*                onChange={() => setSelectedOption("teacher")}*/}
            {/*            />*/}
            {/*            <label htmlFor="teacher">Teacher</label>*/}
            {/*        </div>*/}

            {/*    </div>*/}
                <div className="formField" style={{"marginBottom": "25px"}}>
                    <label className="formFieldCheckboxLabel">
                        <input
                            className="formFieldCheckbox"
                            type="checkbox"
                            name="hasAgreed"
                            value={hasAgreed}
                            onChange={() => {setHasAgreed(!hasAgreed)}}
                        />
                        I agree with {" "}
                        <Link to="null" className="formFieldTermsLink">
                            Terms & Service
                        </Link>
                    </label>
                </div>
            </Form.Group>
            <Form.Group className="d-flex justify-content-center">

                {!passwordsDismatch && !agreeMatch &&  <Form.Text className="text-danger">Please agree with Terms & Service </Form.Text>}
                {accExists &&  <Form.Text className="text-danger">Account with this email already exists. Please, go to "Log in" tab</Form.Text>}
                {succReg &&  <Form.Text className="text-white bg-success rounded p-1">Success! Please check your email to verify your account</Form.Text>}
            </Form.Group>

            <div className={"row justify-content-center my-3"}>

                <Button variant="primary" type="submit" className="justify-content-evenly lg-signup-submit">
                    Sign up
                </Button>
            </div>
        </Form>
        //     <div className="tab-pane fade show active" id="login-tab-pane">
        //
        //         <div className="modal-body-login d-flex flex-column align-items-center w-100">
        //             <div className="account-login-form d-flex flex-column w-100">
        //                 <label htmlfor="user_email_login" className="sidebar-text mt-3"><b>Email</b></label>
        //                 <input className="user_data_input" type="text" placeholder="Enter Email" id="user_email_login"
        //                        required/>
        //                     <label for="user_pass_login" className="sidebar-text mt-3"><b>Password</b></label>
        //                     <input type="password" className="user_data_input" placeholder="Enter Password"
    //                            id="user_pass_login" required/>
    //                         <p id="error-text-log" className="text-danger">Error message here.</p>
    //             </div>
    //         </div>
    //
    //         <div className="modal-footer d-flex flex-row justify-content-around ">
    //             <button type="button" className="btn btn-secondary modal-button-close w-25">Close</button>
    //             <button type="button" className="btn btn-primary w-25 bg-success" id="submit_login">Log in</button>
    //         </div>
    //     </div>
    );
}
