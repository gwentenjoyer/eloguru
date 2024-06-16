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
        //TODO: warning on form that not found
        axios.post(`${process.env.REACT_APP_BASE_URL}/accounts/login`, logData, {withCredentials: true})
            .then(async (res) => {
                if (res.status == 200){
                    // window.location.href = "/profile"
                    localStorage.setItem("account", JSON.stringify())
                    const response = await fetch(`${process.env.REACT_APP_BASE_URL}/accounts/getUserInfo`, { credentials: 'include' });
                    if (!response.ok){
                        console.error("Failed to fetch data user")
                        navigate('/');
                        // return;
                    }
                    const data = await response.json();
                    localStorage.setItem("account", JSON.stringify(data))
                    console.log("saved")
                    // localStorage.setItem("accountRole", JSON.stringify(data))
                }
                window.location.href = "/profile"

                // setEmail(email);
                //     navigate(`/profile`);
                }
        ).catch((err)=> console.error("cound not find account"))
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
