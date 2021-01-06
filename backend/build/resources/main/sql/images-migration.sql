INSERT INTO photos (id, photo_url, owner_id)
SELECT row_number() OVER () as rnum, photo_url, band_id
FROM posts
WHERE photo_url IS NOT NULL;

INSERT INTO posts_photos
SELECT posts.id, photos.id
FROM photos JOIN posts ON posts.photo_url = photos.photo_url;
