import React, { useState } from 'react';

const AddTask = ({ onAdd }) => {
    const [title, setTitle] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!title.trim()) return;
        onAdd(title);
        setTitle('');
    };

    return (
        
        <form onSubmit={handleSubmit} className="flex h-56 w-full bg-white rounded-none border-2 border-pink-100 shadow-xl shadow-pink-100/50 overflow-hidden">
            <input 
                type="text" 
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Add Your Task Here ❤️" 
                className="flex-1 h-full px-12 text-10xl outline-none bg-transparent font-black text-pink-600 placeholder-pink-200 border-none"
            />
            
            {}
            <div className="h-full p-4 bg-transparent">
                <button 
                    type="submit" 
                    className="h-full bg-pink-500 text-white px-16 text-3xl font-black hover:bg-pink-600 transition-all active:scale-95 border-4 border-pink-300 rounded-none"
                >
                    Add
                </button>
            </div>
        </form>
    );
};

export default AddTask;