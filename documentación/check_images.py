import docx

def check_images(file_path):
    doc = docx.Document(file_path)
    images_count = len(doc.inline_shapes)
    print(f"Total inline shapes (images): {images_count}")

if __name__ == "__main__":
    check_images("CRUDs.docx")
