import React, {useEffect, useState} from "react";
import "../../css/AdminComponent.css"
import axios from "axios";

export default function AdminComponent() {

    const [userChanges, setUserChanges] = useState([]);
    const [users, setUsers] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [isEditMode, setIsEditMode] = useState(false);
    const [pageInfo, setPageInfo] = useState({
        page: 0,
        maxPage: 1
    });

    // const fetchUsersData = async (pageInfo) => {
    //     try {
    //         const response = await axios.get(`${process.env.REACT_APP_BASE_URL}/allUser?sort=Id&page=` + pageInfo.page, {withCredentials: true});
    //         if (response.status === 200) {
    //             setUsers(prevUsers => response.data);
    //             setPageInfo(prevState => ({...prevState, maxPage: response.data.totalPages}));
    //             setIsLoading(false);
    //         }
    //     } catch (e) {
    //         console.error(e);
    //     }
    // };
    // useEffect(() => {
    //         fetchUsersData(pageInfo);
    //     },
    //     // eslint-disable-next-line
    //     [pageInfo.page]);

    const handleButtonNextClick = () => {
        if (pageInfo.maxPage - 1 === pageInfo.page) {
            setPageInfo({
                page: 0,
            })
        } else {
            setPageInfo(prevState => ({
                page: prevState.page + 1,
            }))
        }
    }
    const handleButtonPrevClick = () => {
        if (pageInfo.page === 0) {
            setPageInfo({
                page: pageInfo.maxPage - 1,
            })
        } else {
            setPageInfo(prevState => ({
                page: prevState.page - 1,
            }))
        }
    }

    const handleChangeInfo = (userId) => (event) => {
        const {name, value} = event.target;
        setUserChanges(prevState => ({
            ...prevState,
            [userId]: {...prevState[userId], [name]: value}
        }));
    }

    useEffect(() => {

    }, [userChanges, isEditMode]);
    const handleEditButton = () => {
        setIsEditMode(!isEditMode);
    }
    const updateUser = (userId, userChanges) => {
        fetch(`${process.env.REACT_APP_BASE_URL}/allUser/${userId}`, {
            credentials: 'include',
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userChanges)
        })
            .then()
            .catch((error) => {
                console.error('Error:', error);
            });
    }
    const handleSaveButton = () => {
        // eslint-disable-next-line
        // Object.keys(userChanges).map(user => {
        //     if (user.role === undefined) {
        //         userChanges[user].role = document.getElementsByName('role')[user - 1].value;
        //     }
        //     if (user.active === undefined) {
        //         userChanges[user].active = document.getElementsByName('active')[user - 1].value;
        //     }
        // })
        const updatedUsers = Object.keys(userChanges).map(userId => {
            const updatedUser = {...userChanges[userId]};
            if (updatedUser.role === undefined) {
                updatedUser.role = document.getElementsByName('role')[userId - 1].value;
            }
            if (updatedUser.active === undefined) {
                updatedUser.active = document.getElementsByName('active')[userId - 1].value;
            }
            return updateUser(userId, updatedUser);
        });
        // console.log(userChanges)
        // const updatePromises = Object.keys(userChanges).map(user => {
        //     return updateUser(user, userChanges[user]);
        // });
        try {
            Promise.all(updatedUsers);
            // Promise.all(updatePromises);
        } catch (error) {
        }
        window.location.reload();
    }

    return (
        <div className="content" style={{"position": "relative"}}>
            <div style={{"paddingLeft": "18%"}}>
                <button className={isEditMode ? "btn-success btn" : "btn-success btn d-none"}
                        style={{"padding": "1% 4%", "marginBottom": "10px", "float": "left"}}
                        onClick={handleSaveButton}>Зберегти
                </button>
            </div>
            <div style={{"paddingLeft": "18%"}}>
                <button className={isEditMode ? "btn-danger btn" : "btn-warning btn"}
                        style={{"padding": "1% 4%", "marginBottom": "10px", "marginLeft": "10px"}}
                        onClick={handleEditButton}>{isEditMode ? "Скасувати" : "Редагувати"}</button>
            </div>
            <button className={"carousel-control-prev"} onClick={handleButtonPrevClick}>
                <span aria-hidden="true" className="carousel-control-prev-icon"></span>
                <span className="visually-hidden">Previous</span></button>
            <div className="table">
                <table style={{"margin": "auto", "borderCollapse": "initial"}}>
                    <thead>
                    <tr>
                        <th>№</th>
                        <th>E-mail</th>
                        <th>Стан</th>
                        <th>Роль</th>
                    </tr>
                    </thead>
                    {
                        isLoading === true ? <p>Завантаження...</p>
                            :
                            <tbody className="tbody">
                            {users.content.map((user, index) => (
                                <tr key={index}>
                                    <td>{user.id}</td>
                                    <td>{user.email}</td>
                                    <td>{isEditMode ? <select name={"active"} id={"active"} style={{
                                            "border": "none",
                                            "outline": "none",
                                            "borderBottom": "1px solid #445366"
                                        }} onChange={handleChangeInfo(user.id)}
                                                              value={userChanges[user.id]?.active || user.active}>
                                            <option value="true">Активний</option>
                                            <option value="false">Неактивний</option>
                                        </select>
                                        : user.active ? 'Активний' : 'Неактивний'}</td>
                                    <td>{isEditMode ? <select name={"role"} id={"role"} style={{
                                        "border": "none",
                                        "outline": "none",
                                        "borderBottom": "1px solid #445366"
                                    }} onChange={handleChangeInfo(user.id)}
                                                              value={userChanges[user.id]?.role || user.role}>
                                        <option value="USER">Користувач</option>
                                        <option value="STUDENT">Студент</option>
                                        <option value="THEORY_TEACHER">Викладач теорії</option>
                                        <option value="PRACTICAL_TEACHER">Викладач практики</option>
                                        <option value="ADMIN">Адміністратор</option>
                                    </select> : user.role}</td>
                                </tr>
                            ))}
                            </tbody>
                    }
                </table>
            </div>
            <button className={"carousel-control-next"} onClick={handleButtonNextClick}>
                <span aria-hidden="true" className="carousel-control-next-icon"></span>
                <span className="visually-hidden">Next</span></button>
        </div>
    );
}