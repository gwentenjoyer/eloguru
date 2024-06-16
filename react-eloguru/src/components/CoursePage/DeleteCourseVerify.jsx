import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {useNavigate} from "react-router-dom";

export default function DeleteCourseVerify(props) {

    const navigate = useNavigate();
    const handleDelete = async () => {
        const response = await fetch(`${process.env.REACT_APP_BASE_URL}/courses/${props.courseId}/delete`, {credentials: 'include', method: "POST"});

        if (!response.status === 200){
            console.log(response)
            console.error("Failed to enroll")
        }
        console.log("deleted")
        navigate('/course');
        // window.location.href = "/"

    };

    return (
        <Modal
            {...props}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            centered
        >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    Are you sure you want to delete course?
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>
All data will be lost.
                </p>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={props.onHide}>Close</Button>
                <Button onClick={handleDelete}>Delete</Button>
            </Modal.Footer>
        </Modal>
    );
}