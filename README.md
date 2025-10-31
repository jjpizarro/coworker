# coworker
curl -s -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"email":"ana@d.com","password":"P@ssw0rd","roles":["ROLE_ADMIN"]}'

# 2) Login (obtener token)
curl -s -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"email":"ana@d.com","password":"P@ssw0rd"}'
# => { "accessToken": "...", "tokenType":"Bearer", "expiresInSeconds":3600 }

# 3) Consumir endpoint protegido
TOKEN="pega_el_accessToken_aqui"
curl -i -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/spaces?page=0&size=5

# 4) Probar restricción por rol (DELETE requiere ADMIN en SecurityConfig)
curl -i -X DELETE -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/rooms/1