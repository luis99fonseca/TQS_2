
export default function getDataForm(form) {
    const formData = new FormData(form)

    const data = {}
    for (var [key, value] of formData.entries()) {
        data[key] = value
    }

    return data
}
