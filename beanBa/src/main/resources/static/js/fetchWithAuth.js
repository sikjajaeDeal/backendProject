
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

async function fetchWithAuth(url, options = {}) {
    let accessToken = localStorage.getItem("accessToken");
    if (!options.headers) options.headers = {};
    if (accessToken) {
        options.headers["Authorization"] = "Bearer " + accessToken;
    }
    options.credentials = "include";

    let res = await fetch(url, options);

    //토큰 만료시간 확인하기
    const token = localStorage.getItem("accessToken");
    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            console.log(
                "exp:", payload.exp,
                "now:", Math.floor(Date.now()/1000),
                "남은초:", payload.exp - Math.floor(Date.now()/1000)
            );
        } catch (e) {
            console.log("토큰 디코딩 실패:", e);
        }
    } else {
        console.log("accessToken 없음 (로그인 안됨 or 만료)");
    }


    const at = localStorage.getItem("accessToken");
    const rt = localStorage.getItem("refreshToken");
    function decode(token) {
        try {
            const base64Url = token.split('.')[1];
            if (!base64Url) throw new Error("JWT 형식 아님");

            // Base64URL → Base64 변환
            let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            // 패딩 추가 (길이를 4의 배수로 맞추기)
            while (base64.length % 4 !== 0) {
                base64 += '=';
            }

            return JSON.parse(atob(base64));
        } catch (e) {
            console.error("디코딩 실패:", e.message, "| token =", token);
            return null;
        }
    }
    console.log("accessToken payload:", at ? decode(at) : "없음");
    console.log("refreshToken payload:", rt ? decode(rt) : "없음");

    console.log("accessToken:", localStorage.getItem("accessToken"));
    console.log("refreshToken:", localStorage.getItem("refreshToken"));


    // accessToken 만료 체크
    if (res.status === 401) {
        let refreshRes = await fetch("/api/auth/refresh", {
            method: "POST",
            credentials: "include",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("refreshToken")
            }
        });

        if (refreshRes.ok) {
            let data = await refreshRes.json();
            let finalAccessToken = data.accessToken || getCookie("accessToken");

            console.log("새로 발급 받은 엑세스 토큰 = ",data.accessToken)

            // 1. localStorage에 저장 (쿠키에서 뺀 경우도!)
            localStorage.setItem("accessToken", data.accessToken);
            localStorage.setItem("refreshToken", data.refreshToken);

            function decodeJWT(token) {
                return JSON.parse(atob(token.split('.')[1]));
            }
            console.log("스토리지에 저장한 토큰 ",decodeJWT(localStorage.getItem("accessToken")));
            console.log("쿠키에 저장한 토큰 ",decodeJWT(finalAccessToken));

            // 3. 새 accessToken으로 Authorization 헤더 갱신
            options.headers["Authorization"] = "Bearer " + finalAccessToken;

            return fetch(url, options);
        } else {
            location.href = "index.html";
            throw new Error("로그인 만료");
        }
    }

    return res;
}