import {Fragment, useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import axios from "axios";
import {useNavigate} from "react-router-dom";

// import '../../css/Login.css'
export default function AdditionInfo() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [passwordsMatch, setPasswordsMatch] = useState(true);
    const navigate = useNavigate();
    const [selectedOption, setSelectedOption] = useState("student");

    const handleButtonClick = () => {
        console.log(`The selected option is "${selectedOption}".`);
    }
    const handleSubmit = (event) => {
        event.preventDefault();

        if (password !== confirmPassword) {
            setPasswordsMatch(false);
            return;
        }
        const logData = {
            email,
            password
        }
        console.log(logData)
        // If passwords match, send the data to the server
        // Here you can implement your logic to send data to the server
        axios.post(`${process.env.REACT_APP_SERVER_URL}/accounts/signup?role=${selectedOption}`, logData, {withCredentials: true})
            .then(res => {
                    window.location.href = "/profile"
                    // navigate(`/profile`);
                }
            )
    };

    return (
        <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Name:</Form.Label>
                <Form.Control
                    // id={"lg-signup-email"}
                    // type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Enter your fullname" />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Country:</Form.Label>
                <Form.Control
                    placeholder="Enter your phone number"
                    // id={"lg-signup-pw"}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </Form.Group>

            <Form.Group>
                <Form.Label>Register as:</Form.Label>

                <div className="d-flex flex-row mb-3">
                    <div className={"me-2"}>

                        <input
                            type="radio"
                            id="student"
                            name="student"
                            value="student"
                            checked={selectedOption === "student"}
                            onChange={() => setSelectedOption("student")}
                        />
                        <label htmlFor="student"className={"mx-1"}>Student</label>
                    </div>
                    <div className={"me-2"}>

                        <input
                            type="radio"
                            id="teacher"
                            name="teacher"
                            value="teacher"
                            checked={selectedOption === "teacher"}
                            onChange={() => setSelectedOption("teacher")}
                        />
                        <label htmlFor="teacher" className={"mx-1"}>Teacher</label>
                    </div>

                </div>
            </Form.Group>
            <div className={"row justify-content-center"}>

                <Button variant="primary" type="submit" className="justify-content-evenly lg-signup-submit">
                    Create account
                </Button>
            </div>
        </Form>
    );
}
