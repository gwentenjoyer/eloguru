import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

export default function LoginForm() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();
        const logData = {
            email,
            password
        }
        console.log(logData)
        axios.post(`${process.env.REACT_APP_SERVER_URL}/accounts/login`, logData, {withCredentials: true})
            .then(res => {
                    // window.location.href = "/profile"
                    navigate(`/profile`);
                }
        )
    };

    return (
        <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Email:</Form.Label>
                <Form.Control
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Enter your email" />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Password:</Form.Label>
                <Form.Control
                    type="password"
                    placeholder="Enter your password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </Form.Group>
            <div className={"row justify-content-center"}>

                <Button variant="primary" type="submit" className="justify-content-evenly lg-signup-submit">
                    Log in
                </Button>
            </div>
        </Form>
    );
}