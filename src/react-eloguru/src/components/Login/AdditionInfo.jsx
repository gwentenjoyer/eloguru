import {useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import axios from "axios";
import {useNavigate} from "react-router-dom";

// import '../../css/Login.css'
export default function AdditionInfo() {
    const navigate = useNavigate();
    const [selectedOption, setSelectedOption] = useState("student");
    const [country, setCountry] = useState("");
    const [name, setName] = useState("");
    const [phone, setPhone] = useState("");

    const handleSubmit = (event) => {
        event.preventDefault();

        // if (password !== confirmPassword) {
        //     setPasswordsMatch(false);
        //     return;
        // }
        const logData = {
            // email,
            // password
        }
        //todo
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
    //
    // const [state, setState] = useState({
    //     name: "",
    //     surname: "",
    //     age: "",
    //     categoryIds: 0
    // })
    // const [searchParams] = useSearchParams();
    // const [isActive, setIsActive] = useState(false);
    //
    // useEffect(() => {
    //         const fetchActivateCodeInfo = async () => {
    //             try {
    //                 await axios(`${process.env.REACT_APP_SERVER_URL}/activate?activationCode=` + searchParams.get("activationCode"), {withCredentials: true});
    //             } catch (e) {
    //                 setIsActive(true);
    //             }
    //         }
    //         fetchActivateCodeInfo()
    //     }, // eslint-disable-next-line
    //     []);
    //
    //
    // const handleChange = async (event) => {
    //     const value = event.target.type === 'number' ? parseInt(event.target.value) : event.target.value;
    //     setState({
    //         ...state,
    //         [event.target.name]: value
    //     });
    // }
    // const handleChangeOption = async (event) => {
    //     const value = event.target.id === 'categoryIds' ? [parseInt(event.target.value)] : event.target.value;
    //     setState({
    //         ...state,
    //         [event.target.name]: value
    //     });
    // }
    // const handleSubmit = async (event) => {
    //     event.preventDefault();
    //     if (state.age !== "" && state.lastname !== "" && state.categoryIds !== 0 && state.firstname !== "") {
    //         await axios.post(`${process.env.REACT_APP_SERVER_URL}/activate?activationCode=` + searchParams.get("activationCode"), state, {withCredentials: true})
    //             .then(res => {
    //                     window.location.href = "/profile"
    //                 }
    //             );
    //         //TODO: зробити переадресацію || виключити її і зробити кнопку?
    //     }
    // }

    return (
        <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Name:</Form.Label>
                <Form.Control
                    // id={"lg-signup-email"}
                    // type="email"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Enter your fullname" />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Country:</Form.Label>
                <Form.Control
                    placeholder="Enter your phone number"
                    // id={"lg-signup-pw"}
                    value={country}
                    onChange={(e) => setCountry(e.target.value)}
                />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
                <Form.Label>Phone:</Form.Label>
                <Form.Control
                    placeholder="Enter your phone number"
                    // id={"lg-signup-pw"}
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                />
            </Form.Group>


            <div className={"row justify-content-center mt-4"}>

                <Button variant="primary" type="submit" className="justify-content-evenly lg-signup-submit">
                    Create account
                </Button>
            </div>
        </Form>
    );
}

