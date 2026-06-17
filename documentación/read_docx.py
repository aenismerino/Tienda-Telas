import docx

def read_docx(file_path):
    doc = docx.Document(file_path)
    for i, para in enumerate(doc.paragraphs):
        print(f"Para {i}: {para.text}")

if __name__ == "__main__":
    read_docx("CRUDs.docx")
