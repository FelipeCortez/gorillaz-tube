SELECT a.id, a.name, u.id, u.link
FROM l_artist_url au
LEFT JOIN artist a ON a.id = au.artist_id
LEFT JOIN url u ON u.id = au.url_id
WHERE a.name LIKE '%Afrika Bambaataa%'
LIMIT 50;
