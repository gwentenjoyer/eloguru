import axios from "axios";

const getRefreshTokens = async () => {
    try {
        const response = await axios.get(`/accounts/refreshToken`, {withCredentials: true});
        // return true;
        if (response.status === 200) {
            return true;
        }else{
            return false;
        }
    }catch (error){
        return false;
    }
}
// TODO: DOES NOTHING
export default getRefreshTokens;
