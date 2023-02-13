export function getDurationString(start, end){
    let duration = (new Date(end).valueOf() - new Date(start).valueOf()) / 1000 / 60
    let hours = Math.trunc(duration / 60)
    let minutes = Math.trunc(duration % 60)
    return (hours > 0 ? hours + '小时' : '') + minutes + '分钟'
}

export default {
    getDurationString,
}
