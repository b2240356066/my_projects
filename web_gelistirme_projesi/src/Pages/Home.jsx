import React, { useState } from 'react';
import AddTask from '../Components/AddTask';
import TaskItem from '../Components/TaskItem';

const Home = () => {
    const [tasks, setTasks] = useState([]);

    const addTask = (title) => {
        const newTask = { id: Date.now(), title, isCompleted: false };
        setTasks([...tasks, newTask]);
    };

    const toggleTask = (id) => {
    setTasks(tasks.map(task => 
        // If the ID matches, return a NEW object with the updated status
        task.id === id ? { ...task, isCompleted: !task.isCompleted } : task
    ));
    };

    const deleteTask = (id) => {
        setTasks(tasks.filter(t => t.id !== id));
    };

    const updateTask = (id, newTitle) => {
    setTasks(tasks.map(t => 
        t.id === id ? { ...t, title: newTitle } : t
    ));
};

    return (
        <div className="w-full min-h-screen bg-[#fdf2f8] p-10 flex flex-col items-center">
            {/* Header */}
            <div className="text-center mb-16">
                <h1 className="text-5xl font-black text-pink-600 mb-2">
                    My To-Do List 🌸
                </h1>
                <p className="text-pink-400 text-lg font-medium italic">Software Persona 2. Week Project</p>
            </div>

            {/* Content Area*/}
            <div className="w-full max-w-6xl">
                <AddTask onAdd={addTask} />
                
                <div className="mt-10">
                    {tasks.length === 0 ? (
                        <div className="py-10 text-center">
                            <p className="text-pink-400 text-3xl font-bold italic">
                                Add your first task! You have a lot to finish !!!
                            </p>
                        </div>
                    ) : (
                        
                        tasks.map(task => (
                            <TaskItem 
                                key={task.id} 
                                task={task} 
                                onToggle={toggleTask} 
                                onDelete={deleteTask} 
                                onUpdate={updateTask}
                            />
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default Home;