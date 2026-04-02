-- 修改 scenic 表的 images 字段类型为 LONGTEXT，以支持存储 Base64 图片数据
ALTER TABLE scenic MODIFY COLUMN images LONGTEXT;

-- 修改 travel_route 表的 images 字段类型为 LONGTEXT（如果路线表也有图片字段）
ALTER TABLE travel_route MODIFY COLUMN images LONGTEXT;
