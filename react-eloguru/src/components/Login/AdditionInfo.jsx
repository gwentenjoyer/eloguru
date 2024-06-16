import {useEffect, useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import axios from "axios";
import {useNavigate, useSearchParams} from "react-router-dom";

// import '../../css/Login.css'
export default function AdditionInfo() {
    const navigate = useNavigate();
    const [selectedOption, setSelectedOption] = useState("student");

    const [state, setState] = useState({
        fullname: "",
        country: "",
        phone: ""
    })
    const [searchParams] = useSearchParams();
    const [isActive, setIsActive] = useState(false);
    const [isEmpty, setIsEmpty] = useState(false);

    useEffect(() => {
            const fetchActivateCodeInfo = async () => {
                try {
                    await axios(`${process.env.REACT_APP_SERVER_URL}/accounts/activate?activationCode=` + searchParams.get("activationCode"), {withCredentials: true});
                } catch (e) {
                    setIsActive(true);
                }
            }
            fetchActivateCodeInfo()
        }, // eslint-disable-next-line
        []);


    const handleChange = async (event) => {
        const value = event.target.type === 'number' ? parseInt(event.target.value) : event.target.value;
        setState({
            ...state,
            [event.target.name]: value
        });
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        console.log(state)
        if (state.fullname !== "" && state.country !== "" && state.phone !== "") {
            await axios.post(`${process.env.REACT_APP_SERVER_URL}/accounts/activate?activationCode=${searchParams.get("activationCode")}`, state, {withCredentials: true})
                .then(res => {
                    // window.location.href = "/profile"
                    navigate(`/profile`);
                    }
                )
                .catch(e => console.log(e))
        }
        else setIsEmpty(true)
    }

    if (isActive) {
        return (
            <div className={"container text-center "} style={{"borderRadius": "20px", "maxWidth": "380px","marginTop":"70px","marginBottom":"70px"}}>
                <p style={{"color": "black", "marginBottom": "0px", "paddingTop": "10px"}}>Link is no longer available</p>
                <p style={{"color": "black", "paddingBottom": "10px"}}></p></div>);
    }


    return (
        <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Name:</Form.Label>
                <Form.Control
                    // id={"lg-signup-email"}
                    // type="email"
                    name={"fullname"}
                    value={state.fullname}
                    onChange={handleChange}
                    placeholder="Enter your fullname" />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Country:</Form.Label>
                <Form.Control
                    placeholder="Enter your phone number"
                    name={"country"}
                    // id={"lg-signup-pw"}
                    value={state.country}
                    onChange={handleChange}
                />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Phone:</Form.Label>
                <Form.Control
                    placeholder="Enter your phone number"
                    // id={"lg-signup-pw"}
                    name={"phone"}
                    value={state.phone}
                    onChange={handleChange}
                />
            </Form.Group>

            <Form.Group className="d-flex justify-content-center">

                {isEmpty &&  <Form.Text className="text-danger">Please fill all fields</Form.Text>}
            </Form.Group>


            <div className={"row justify-content-center mt-4"}>

                <Button variant="primary" type="submit" className="justify-content-evenly lg-signup-submit">
                    Create account
                </Button>
            </div>
        </Form>
    );
}

