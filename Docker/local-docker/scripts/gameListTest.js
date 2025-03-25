import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = { //Reserved Variable
    stages: [
        { duration: '10s', target: 10 }, // Ramp up to 10 users (VUs --> Virtual Users)
        { duration: '20s', target: 50 }, // Ramp up to 50 users //Will add up to existing VUs
        { duration: '10s', target: 0 }   // Ramp down to 0 users
    ]
};

export default function () { //Reserved EntryPoint
    let res = http.post('http://localhost:8888/api/v1/game/all');

    check(res, {
        'is status 200': (r) => r.status === 200,
        'response time < 500ms': (r) => {
            if (r.timings.duration >= 500) {
                console.log(`❌ Slow request: ${r.timings.duration} ms`);
            }
            return r.timings.duration < 500;
        }
    });

    sleep(1); // Pause for 1 second before next request
}
