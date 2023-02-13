import Vue from "vue";

// 判断参数是否是其中之一
export function oneOf(value, validList) {
    for (let i = 0; i < validList.length; i++) {
        if (value === validList[i]) {
            return true;
        }
    }
    return false;
}

export function deepClone(data) {
    const type = judgeType(data);
    let obj;
    if (type === 'array') {
        obj = [];
    } else if (type === 'object') {
        obj = {};
    } else {
        // 不再具有下一层次
        return data;
    }
    if (type === 'array') {
        // eslint-disable-next-line
        for (let i = 0, len = data.length; i < len; i++) {
            obj.push(deepClone(data[i]));
        }
    } else if (type === 'object') {
        // 对原型上的方法也拷贝了....
        // eslint-disable-next-line
        for (const key in data) {
            obj[key] = deepClone(data[key]);
        }
    }
    return obj;
}

function judgeType(obj) {
    // tostring会返回对应不同的标签的构造函数
    const toString = Object.prototype.toString;
    const map = {
        '[object Boolean]': 'boolean',
        '[object Number]': 'number',
        '[object String]': 'string',
        '[object Function]': 'function',
        '[object Array]': 'array',
        '[object Date]': 'date',
        '[object RegExp]': 'regExp',
        '[object Undefined]': 'undefined',
        '[object Null]': 'null',
        '[object Object]': 'object',
    };
    if (obj instanceof Element) {
        return 'element';
    }
    return map[toString.call(obj)];
}

export function downloadFile (url) {
    return new Promise((resolve, reject) => {
        Vue.prototype.$http.get(url, {responseType: 'blob'}).then(resp => {
            let headers = resp.headers
            if(!headers['content-type']){
                Vue.prototype.errorNotify('No Content-Type')
            }
            if (headers['content-type'].indexOf('json') !== -1) {
                let fr = new window.FileReader()
                if (resp.data.error) {
                    Vue.prototype.errorNotify(resp.data.error)
                } else {
                    Vue.prototype.errorNotify('Invalid file format')
                }
                fr.onload = (event) => {
                    let data = JSON.parse(event.target.result)
                    if (data.error) {
                        Vue.prototype.errorNotify(data.data)
                    } else {
                        Vue.prototype.errorNotify('Invalid file format')
                    }
                }
                let b = new window.Blob([resp.data], {type: 'application/json'})
                fr.readAsText(b)
                return
            }
            console.log(resp);
            let link = document.createElement('a')
            link.href = window.URL.createObjectURL(new window.Blob([resp.data], {type: headers['content-type']}))
            link.download = (headers['content-disposition'] || '').split('filename=')[1]
            document.body.appendChild(link)
            link.click()
            link.remove()
            resolve()
        }).catch(() => {})
    })
}