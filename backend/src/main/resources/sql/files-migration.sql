INSERT INTO files (id, file_url, owner_id)
SELECT row_number() OVER () as rnum, file_url, band_id
FROM posts
WHERE file_url IS NOT NULL;

INSERT INTO posts_files
SELECT posts.id, files.id
FROM files JOIN posts ON posts.file_url = files.file_url;
