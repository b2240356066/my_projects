import React, { useState } from 'react';

const TaskItem = ({ task, onToggle, onDelete, onUpdate }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [newText, setNewText] = useState(task.title);

    const handleSave = () => {
        onUpdate(task.id, newText);
        setIsEditing(false);
    };

    return (
        <div className="flex justify-between items-center p-8 bg-white/90 rounded-none border border-pink-50 mb-4 shadow-sm">
            <div className="flex items-center gap-6 flex-1">
                <input 
                    type="checkbox" 
                    checked={task.isCompleted}
                    onChange={() => onToggle(task.id)}
                    className="w-8 h-8 accent-pink-500 cursor-pointer"
                />

                {isEditing ? (
                    
                    <input 
                        className="text-2xl font-bold text-pink-600 border-b-2 border-pink-400 outline-none w-full bg-transparent"
                        value={newText}
                        onChange={(e) => setNewText(e.target.value)}
                        autoFocus
                    />
                ) : (
                    
                    <span className={`text-2xl font-bold ${
                        task.isCompleted ? 'line-through text-pink-200 italic opacity-50' : 'text-gray-700'
                    }`}>
                        {task.title}
                    </span>
                )}
            </div>

            <div className="flex gap-4">
                <button 
                    onClick={() => isEditing ? handleSave() : setIsEditing(true)}
                    className="text-pink-500 font-bold text-lg border-2 border-pink-100 px-4 py-2 hover:bg-pink-50 rounded-none"
                >
                    {isEditing ? 'Save ✨' : 'Edit'}
                </button>
                <button 
                    onClick={() => onDelete(task.id)}
                    className="text-rose-400 font-bold text-lg border-2 border-rose-50 px-4 py-2 hover:bg-rose-50 rounded-none"
                >
                    Delete 🗑️
                </button>
            </div>
        </div>
    );
};

export default TaskItem;