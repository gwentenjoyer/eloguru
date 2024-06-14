import { useState } from 'react';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import LoginForm from "./LoginForm";
import SignupForm from "./SignupForm";
import AdditionInfo from "./AdditionInfo";

export default function ControlledTabsExample() {
    const [key, setKey] = useState('login');

    return (
        <Tabs
            id="controlled-tab-example"
            activeKey={key}
            onSelect={(k) => setKey(k)}
            // defaultActiveKey={}
            className="mb-3 modal-login modal-content flex-row"
            justify
        >
            <Tab eventKey="login" title="Log in" className="mynav-link">
                <LoginForm/>
            </Tab>
            <Tab eventKey="signup" title="Sign up">
                <SignupForm/>
                {/*<AdditionInfo/>*/}
            </Tab>
        </Tabs>
    );
}
